package ru.corthos.corbit.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

public record MetaFile(String name, String extension, List<String> content, XWPFDocument doc) {
}
