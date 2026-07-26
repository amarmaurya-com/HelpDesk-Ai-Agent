import React, { useState } from "react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { useNavigate } from "react-router-dom";

function ChatHome() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");

  const handleChatStartClick = () => {
    const userEmail = email.trim().toLowerCase();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(userEmail)) {
      alert("Please enter a valid email address.");
      return;
    }

    localStorage.setItem("userEmail", userEmail);
    navigate("/chat");
  };

  return (
    <div className="relative flex h-screen w-screen items-center justify-center overflow-hidden">
      {/* Background Video */}
      <video
        autoPlay
        loop
        muted
        playsInline
        className="absolute inset-0 h-full w-full scale-110 object-cover blur-md"
      >
        <source src="/HomePage.mp4" type="video/mp4" />
      </video>

      {/* Dark Overlay */}
      <div className="absolute inset-0 bg-black/60"></div>

      {/* Content */}
      <div className="relative z-10 flex w-full max-w-md flex-col items-center gap-6 rounded-2xl border border-white/10 bg-white/10 p-8 backdrop-blur-md">
        <h1 className="text-center text-4xl font-bold text-white">
          Help Desk Agent
        </h1>

        <p className="text-center text-sm text-gray-300">
          Enter your email address to start chatting with the AI assistant.
        </p>

        <Input
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleChatStartClick();
            }
          }}
          className="bg-white/20 text-white placeholder:text-gray-300"
        />

        <Button
          onClick={handleChatStartClick}
          className="w-full"
        >
          Start Chat
        </Button>
      </div>
    </div>
  );
}

export default ChatHome;