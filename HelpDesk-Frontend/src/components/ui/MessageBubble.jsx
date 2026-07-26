import React from 'react'

function MessageBubble({ index, author, at, children }) {
    const isSender = author;
  return (
    <div className={`flex text-accent ${isSender === "bot" ? "justify-start" : "justify-end"}`}>
        <div className={`max-w-[70%] rounded-lg p-3 ${isSender === "user" ? "bg-gray-600 text-gray-100" : "bg-yellow-900 text-white"}`}>
            <p className="whitespace-pre-wrap break-word leading-relaxed">{children}</p>
            <div>
                <span className={`mt-1 text-[10px] ${isSender === "user" ? "text-gray-500" : "text-blue-300"}`}>{at}</span>
            </div>
        </div>
    </div>
  )
}

export default MessageBubble
