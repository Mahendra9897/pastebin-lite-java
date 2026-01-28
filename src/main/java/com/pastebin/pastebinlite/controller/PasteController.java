package com.pastebin.pastebinlite.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

import com.pastebin.pastebinlite.repository.PasteRepository;
import com.pastebin.pastebinlite.entity.Paste;
import com.pastebin.pastebinlite.dto.*;
import com.pastebin.pastebinlite.service.PasteService;

import java.util.Map;
import java.util.HashMap;

@RestController
public class PasteController {

 private final PasteRepository repo;
 private final PasteService service;

 public PasteController(PasteRepository repo, PasteService service) {
  this.repo = repo;
  this.service = service;
 }

 // ---------------- HEALTH ----------------
 @GetMapping("/api/healthz")
 public Map<String, Boolean> health() {
  Map<String, Boolean> map = new HashMap<>();
  map.put("ok", true);
  return map;
 }

 // ---------------- CREATE PASTE ----------------
 @PostMapping("/api/pastes")
 public PasteResponse create(@RequestBody PasteRequest req, HttpServletRequest r) {

  if (req.content == null || req.content.isBlank())
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

  Paste p = new Paste();
  p.setContent(req.content);

  if (req.ttl_seconds != null)
   p.setExpiresAt(service.now(r).plusSeconds(req.ttl_seconds));

  p.setMaxViews(req.max_views);

  repo.save(p);

  PasteResponse res = new PasteResponse();
  res.id = p.getId();
  res.url = "http://localhost:8080/p/" + p.getId();   // HTML URL

  return res;
 }

 // ---------------- FETCH API ----------------
 @GetMapping("/api/pastes/{id}")
 public Map<String, Object> fetch(@PathVariable String id, HttpServletRequest r) {

  Paste p = service.getValidPaste(id, r);

  p.setViews(p.getViews() + 1);
  repo.save(p);

  Map<String,Object> map = new HashMap<>();
  map.put("content", p.getContent());
  map.put("remaining_views", p.getMaxViews() == null ? null : p.getMaxViews() - p.getViews());
  map.put("expires_at", p.getExpiresAt());

  return map;
 }

 // ---------------- HTML VIEW ----------------
 @GetMapping("/p/{id}")
 public ResponseEntity<String> view(@PathVariable String id, HttpServletRequest r) {

  Paste p = service.getValidPaste(id, r);

  p.setViews(p.getViews() + 1);
  repo.save(p);

  String safe = HtmlUtils.htmlEscape(p.getContent());

  return ResponseEntity.ok("<pre>" + safe + "</pre>");
 }
}
