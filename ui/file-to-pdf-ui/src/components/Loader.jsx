import React from "react";

export default function Loader() {
  return (
    <div className="flex justify-center items-center py-8">
      <div className="w-10 h-10 border-4 border-indigo-400 border-t-transparent rounded-full animate-spin"></div>
      <p className="ml-4 text-indigo-600 font-medium">Конвертация...</p>
    </div>
  );
}
