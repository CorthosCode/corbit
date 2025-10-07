import { useState } from "react";

function App() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [pdfUrl, setPdfUrl] = useState(null);
  const [error, setError] = useState("");

  const handleUpload = async () => {
    if (!file) return;

    setLoading(true);
    setError("");
    setPdfUrl(null);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("/upload", {
        method: "POST",
        body: formData,
        // Не устанавливаем Content-Type - браузер сам установит с boundary
      });

      if (!response.ok) {
        throw new Error(`Ошибка сервера: ${response.status}`);
      }

      // Получаем PDF как blob
      const blob = await response.blob();

      // Создаем URL для скачивания
      const url = URL.createObjectURL(blob);
      setPdfUrl(url);

    } catch (err) {
      setError(err.message || "Произошла ошибка при конвертации");
      console.error("Upload error:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleFileSelect = (selectedFile) => {
    setFile(selectedFile);
    setPdfUrl(null);
    setError("");
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center p-4">
      <div className="bg-white/20 backdrop-blur-lg rounded-3xl shadow-2xl border border-white/30 p-8 w-full max-w-md">
      <img src="/src/img/corbit.png" alt="Corbit Logo" className="w-40 max-w-xs mx-auto "/>
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-white mb-2">
            PDF-конвертер
          </h1>
          <p className="text-white/80">
            Перетащите файл или выберите вручную
          </p>
        </div>

        {/* Drag & Drop Area */}
        <div className="space-y-6">
          <div
            onClick={() => !loading && document.getElementById('fileInput').click()}
            className="border-2 border-dashed border-white/40 rounded-2xl p-8 text-center cursor-pointer transition-all bg-white/10 hover:bg-white/15"
          >
            <svg className="w-12 h-12 text-white mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
            <p className="text-white font-medium text-lg mb-2">
              {file ? file.name : "Перетащите файл сюда"}
            </p>
            <p className="text-white/70">
              или <span className="text-white font-semibold">кликните для выбора</span>
            </p>
            <p className="text-white/50 text-sm mt-2">
              DOC, DOCX, XLS, XLSX, PPT, TXT, RTF
            </p>
          </div>

          <input
            id="fileInput"
            type="file"
            onChange={(e) => handleFileSelect(e.target.files[0])}
            className="hidden"
            accept=".txt,.doc,.docx,.odt,.xls,.xlsx,.ppt,.pptx,.rtf"
          />

          {/* File Info */}
          {file && (
            <div className="bg-white/10 rounded-xl p-4 border border-white/20">
              <div className="flex items-center justify-between text-white">
                <div className="flex items-center gap-3">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                  <span className="font-medium truncate">{file.name}</span>
                </div>
                <button
                  onClick={() => setFile(null)}
                  className="text-white/60 hover:text-white transition-colors"
                >
                  ×
                </button>
              </div>
            </div>
          )}

          {/* Convert Button */}
          <button
            onClick={handleUpload}
            disabled={!file || loading}
            className={`w-full py-4 rounded-xl font-semibold text-white text-lg transition-all ${
              file && !loading
                ? "bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 shadow-lg hover:shadow-xl"
                : "bg-gray-500 cursor-not-allowed"
            }`}
          >
            {loading ? (
              <span className="flex items-center justify-center gap-3">
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                Конвертация...
              </span>
            ) : (
              "Конвертировать в PDF"
            )}
          </button>

          {/* Download Link */}
          {pdfUrl && (
            <div className="text-center animate-fade-in">
              <div className="flex items-center justify-center gap-2 mb-3 text-green-100">
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                </svg>
                <span className="font-semibold">Файл успешно сконвертирован!</span>
              </div>
              <a
                href={pdfUrl}
                download={file?.name.replace(/\.[^/.]+$/, ".pdf")}
                className="inline-flex items-center gap-2 px-6 py-3 bg-green-500 text-white rounded-lg font-semibold hover:bg-green-600 transition-colors shadow-lg"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 16l-4-4m0 0l4-4m-4 4h8m-4 4v6" />
                </svg>
                Скачать PDF
              </a>
            </div>
          )}

          {/* Error Message */}
          {error && (
            <div className="p-3 bg-red-500/20 border border-red-500/30 rounded-lg text-red-100 text-center">
              <div className="flex items-center justify-center gap-2">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {error}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;