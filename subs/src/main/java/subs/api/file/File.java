package subs.api.file;

import subs.api.user.User;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Map;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity(name = "file")
@Table(name = "files")
public class File {

    @Id
    @SequenceGenerator(
            name = "files_sequence",
            sequenceName = "files_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "files_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "File name must not be empty")
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "date", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date date;

    @ElementCollection
    @CollectionTable(name = "file_mapping",
            joinColumns = {@JoinColumn(name = "file_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "word")
    @Column(name = "translation")
    private Map<String, String> words;

    @NotNull(message = "The file must be specific to a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public File(String name, Date date, Map<String, String> words, User user) {
        this.name = name;
        this.date = date;
        this.words = words;
        this.user = user;
    }
}
