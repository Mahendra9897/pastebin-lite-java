
	package com.pastebin.pastebinlite.entity;

	import jakarta.persistence.*;
	import java.time.Instant;

	@Entity
	public class Paste {

	 @Id
	 @GeneratedValue(strategy = GenerationType.UUID)
	 private String id;

	 @Column(nullable=false)
	 private String content;

	 private Instant expiresAt;

	 private Integer maxViews;

	 private Integer views = 0;

	 private Instant createdAt = Instant.now();

	 public String getId() {
	  return id;
	 }

	 public void setId(String id) {
	  this.id = id;
	 }

	 public String getContent() {
	  return content;
	 }

	 public void setContent(String content) {
	  this.content = content;
	 }

	 public Instant getExpiresAt() {
	  return expiresAt;
	 }

	 public void setExpiresAt(Instant expiresAt) {
	  this.expiresAt = expiresAt;
	 }

	 public Integer getMaxViews() {
	  return maxViews;
	 }

	 public void setMaxViews(Integer maxViews) {
	  this.maxViews = maxViews;
	 }

	 public Integer getViews() {
	  return views;
	 }

	 public void setViews(Integer views) {
	  this.views = views;
	 }

	 public Instant getCreatedAt() {
	  return createdAt;
	 }

	 public void setCreatedAt(Instant createdAt) {
	  this.createdAt = createdAt;
	 }
	}



