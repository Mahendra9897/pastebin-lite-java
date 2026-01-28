package com.pastebin.pastebinlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pastebin.pastebinlite.entity.Paste;

public interface PasteRepository extends JpaRepository<Paste, String> {

}
