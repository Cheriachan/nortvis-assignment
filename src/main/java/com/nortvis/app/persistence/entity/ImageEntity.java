package com.nortvis.app.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Table(name = "images")
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "imgur_id")
  private String imgurId;

  private String url;

  @Column(name = "delete_hash")
  private String deleteHash;

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Column(name = "created_at")
  private Long createdAt;
}
