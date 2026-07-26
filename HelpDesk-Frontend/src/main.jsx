import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import App from "./App";
import Chat from "./Pages/Chat";
import ChatHome from "./Pages/ChatHome";

createRoot(document.getElementById("root")).render(
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/chat-home" element={<ChatHome />} />
      </Routes>
    </BrowserRouter>
);