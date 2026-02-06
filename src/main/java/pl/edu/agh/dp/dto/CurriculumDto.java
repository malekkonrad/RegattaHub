package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Curriculum;
import pl.edu.agh.dp.entity.Document;

import java.time.LocalDate;

/**
 * DTO dla Cirriculum (CV).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class CurriculumDto extends DocumentDto {

    private String name;
    private String surname;
    private LocalDate creationDate;

    @Override
    public Document toEntity() {
        Curriculum entity = new Curriculum();
        fillCommonFields(entity);
        entity.setName(this.name);
        entity.setSurname(this.surname);
        entity.setCreationDate(this.creationDate);
        return entity;
    }

    /**
     * Tworzy DTO z encji Cirriculum.
     */
    public static CurriculumDto fromEntity(Curriculum entity) {
        if (entity == null) return null;
        return CurriculumDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .content(entity.getContent())
                .name(entity.getName())
                .surname(entity.getSurname())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
