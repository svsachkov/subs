package subs.api.file;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FileRequest {

    private final String name;
    private final Date date;
}
