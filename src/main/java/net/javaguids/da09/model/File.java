package net.javaguids.da09.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "file_name", nullable = false)
    String fileName;

    @Column(name = "upload_at", nullable = false)
   LocalDateTime uploadAt;

//    @Column(name = "id_user_upload", nullable = false)
//    Long idUserUpload;
}
