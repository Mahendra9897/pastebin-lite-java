package com.pastebin.pastebinlite.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

import com.pastebin.pastebinlite.entity.Paste;
import com.pastebin.pastebinlite.repository.PasteRepository;

import java.time.Instant;

@Service
public class PasteService {

 private final PasteRepository repo;

 public PasteService(PasteRepository repo) {
  this.repo = repo;
 }

 public Instant now(HttpServletRequest req) {

  if ("1".equals(System.getenv("TEST_MODE"))) {
   String h = req.getHeader("x-test-now-ms");
   if (h != null) {
    return Instant.ofEpochMilli(Long.parseLong(h));
   }
  }

  return Instant.now();
 }

 public Paste getValidPaste(String id, HttpServletRequest req) {

  Paste p = repo.findById(id)
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

  Instant current = now(req);

  if (p.getExpiresAt() != null && current.isAfter(p.getExpiresAt()))
   throw new ResponseStatusException(HttpStatus.NOT_FOUND);

  if (p.getMaxViews() != null && p.getViews() >= p.getMaxViews())
   throw new ResponseStatusException(HttpStatus.NOT_FOUND);

  return p;
 }
}
