import pandas as pd
import srt
import warnings
import string
import nltk
import sys
import spacy
import requests
from spacy.lang.en.examples import sentences

nltk.download('stopwords')


def translate(texts):
    IAM_TOKEN = get_iam_token()
    folder_id = 'b1got1e9ura7lrubkir9'
    target_language = 'ru'

    body = {
        "targetLanguageCode": target_language,
        "texts": texts,
        "folderId": folder_id,
    }

    headers = {
        "Content-Type": "application/json",
        "Authorization": "Bearer {0}".format(IAM_TOKEN)
    }

    response = requests.post('https://translate.api.cloud.yandex.net/translate/v2/translate',
                             json=body,
                             headers=headers
                             )

    return response


def get_iam_token():
    headers = {'Content-Type': 'application/x-www-form-urlencoded', }
    data = '{"yandexPassportOauthToken":"AQAAAABRifOPAATuwakY-M_p50s4nIzyJB2mHwk"}'

    response = requests.post('https://iam.api.cloud.yandex.net/iam/v1/tokens', headers=headers, data=data)
    iam = response.json()['iamToken']

    return iam


def process(file):
    warnings.filterwarnings("ignore")
    filepath = file.path
    with open(filepath, encoding='utf-8-sig') as f:
        sub_text = f.read()

    ###
    subs = srt.parse(sub_text)

    subs = list(subs)
    for sub in subs:
        sub.content = sub.content.replace('\n', ' ')

    ###
    data = pd.DataFrame()

    for sub in subs:
        df = pd.DataFrame({'start': [sub.start],
                           'end': [sub.end],
                           'content': [sub.content],
                           })
        data = data.append(df)

    ##
    data.reset_index(drop=True, inplace=True)

    # run tokenization and data cleaning
    texts = process_data(data['content'])

    ##
    data['tokens'] = texts

    nlp = spacy.load('en_core_web_sm', disable=['parser', 'ner'])
    lemmas = []
    for tokens in data['tokens']:
        a = []
        for token in tokens:
            doc = nlp(token)
            lemma = " ".join([token.lemma_ for token in doc])
            if lemma == '-PRON-':
                continue
        a.append(lemma)
        lemmas.append(a)

    ##
    data['lemmas'] = lemmas

    ##
    word_freq = pd.read_csv('C:\\Users\\stepa\\OneDrive\\Рабочий стол\\subs\\subs\\src\\main\\python\\unigram_freq.csv')

    ##
    all_words = []

    ##
    for i in data['lemmas']:
        all_words.extend(i)

    ##
    mapping = pd.DataFrame(all_words, columns=['word'])
    mapping = mapping.drop_duplicates(subset=['word'])
    mapping.reset_index(inplace=True, drop=True)

    ##
    rank = []

    for i in range(0, mapping.shape[0]):
        try:
            rank.append(word_freq.loc[word_freq['word'] == mapping['word'].loc[i]].index[0])
        except:
            rank.append(-1)

    ##
    mapping['rank'] = rank

    ##
    mapping = mapping[mapping['rank'] != -1]

    ##
    mapping = mapping.sort_values(by=['rank'], ascending=False)
    mapping.reset_index(inplace=True, drop=True)

    # Process completed

    # TRANSLATE
    mapping = mapping.head(100)
    words_to_translate = list(mapping['word'])
    response = translate(words_to_translate)

    translations = []
    for translation in response.json()['translations']:
        translations.append(translation['text'])

    mapping['russian'] = translations

    # FILE output
    # file_name = file.name
    file_name = "result.srt"
    file_name = file_name.split("/")[-1]
    file_name = file_name.rsplit(".", 1)[0]
    file_name += '.xlsx'

    # Drop rank column
    mapping.drop(columns=['rank'], inplace=True)

    # Rename column
    mapping.rename(columns={"word": "english"}, inplace=True)

    mapping.to_excel(file_name)


def process_data(data):
    # nltk.download('stopwords')
    stop_words = nltk.corpus.stopwords.words('english')
    word_tokenizer = nltk.WordPunctTokenizer()
    texts = []
    targets = []

    for item in data:
        tokens = word_tokenizer.tokenize(item)
        tokens = [word for word in tokens if
                  (word not in string.punctuation and word not in stop_words and not word[0].isupper())]

        texts.append(tokens)

    return texts


process(sys.argv[1])
print("DOOOOONEEEEE")
