import React, { useEffect } from 'react'
import { Button } from '../components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar"
import { Input } from "../components/ui/input"
import { ScrollArea } from "../components/ui/scroll-area"
import { MoreVertical, Plus, Search, Send } from "lucide-react"
import MessageBubble from '../components/ui/MessageBubble'
import { useState } from 'react'
import { sendMessageToServer } from '../service/chat.service'
import { Spinner } from "../components/ui/spinner"
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { useNavigate } from 'react-router-dom'

const CHAT = [
  {
    id:1,
    name: "John Doe",
    lastMessage: "Hello, how can I help you?",
    unread: 5,
    initials: "JD",
  },
  {
    id:2,
    name: "Jane Smith",
    lastMessage: "Thanks for your help!",
    unread: 3,
    initials: "JS",
  },
  {
    id:3,
    name: "Mike Johnson",
    lastMessage: "Can you check the status?",
    unread: 2,
    initials: "MJ",
  },
  {
    id:4,
    name: "Sarah Wilson",
    lastMessage: "Issue resolved, thank you!",
    unread: 0,
    initials: "SW",
  },
  {
    id:5,
    name: "Robert Brown",
    lastMessage: "When will it be fixed?",
    unread: 7,
    initials: "RB",
  },
]

const CONVERCATION = [
  {
    id: 1,
    author: "bot",
    text: "Hello, how can I help you?",
    at: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
  },
]

function Chat() {

  const [messages, setmessages] = useState(CONVERCATION);
  const [draft, setdraft] = useState("");
  const endRef = React.useRef(null);
  const [activeChat, setactiveChat] = useState(CHAT[0])
  const [sending, setsending] = useState(false);
  const [email, setEmail] = useState("");
  const inputRef = React.useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  useEffect(() => {
    setEmail(localStorage.getItem("userEmail") || "");
    inputRef.current?.focus();
  }, []);

  async function sendMessage() {
    const textMessage = draft.trim();
    if (!textMessage) return;
    console.log(draft);
    setsending(true);

    setmessages(prevMessages => [
      ...prevMessages,
      {
        id: prevMessages.length + 1,
        author: "user",
        text: draft,
        at: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
      }
    ])

    const responseFromAi = await sendMessageToServer(textMessage, email);
    console.log(responseFromAi);

    setmessages( prevMessages => [
      ...prevMessages,
      {
        id: prevMessages.length + 1,
        author: "bot",
        text: responseFromAi,
        at: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
      }
    ]);
    setsending(false);
    setdraft("");
    inputRef.current?.focus();
  }

  const handleLogout = ()=>{
    localStorage.clear();
    navigate("/chat-home")
  }

  const initials = email
  .split("@")[0]
  .split(/[._-]/)
  .map(word => word[0]?.toUpperCase())
  .join("")
  .slice(0, 2);
  return (
   <div className="fixed inset-0 rounded-xl mx-auto max-w-5xl grid grid-cols-1 md:grid-cols-[300px_minmax(0,_1fr)] border-x">
  <aside className="border-r rounded-xl relative overflow-hidden">
  <video
    autoPlay
    loop
    muted
    playsInline
    className="absolute inset-0 h-full w-full object-cover"
  >
    <source src="/aSide.mp4" type="video/mp4" />
  </video>
      <div className="absolute inset-0 bg-black/90"></div>
    <div className="p-3 flex items-center gap-2">
      <div className="relative w-full">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />

        <input
          type="text"
          placeholder="Search..."
          className="w-full h-9 rounded-2xl border pl-9 pr-3"
        />
      </div>
    </div>
    <ScrollArea className="flex">
      <ul className='p-2 space-y-1'>
        {CHAT.map((c) => (
          <li key={c.id}>
            <button
              onClick={() => setactiveChat(c)}
              className={`w-full rounded-xl px-3 py-2 text-left hover:bg-accent transition ${
                activeChat.id === c.id ? "bg-accent" : ""
              }`}
            >
              <div className="flex items-center gap-3">
                <Avatar className="h-9 w-9">
                  <AvatarImage src="" alt={c.name} />
                  <AvatarFallback className="text-xs">
                    {c.initials}
                  </AvatarFallback>
                </Avatar>
                <div className="min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <span className="truncate text-sm font-medium">
                      {c.name}
                    </span>
                    {c.unread ? (
                      <span className="ml-auto inline-flex h-5 min-w-5 items-center justify-center rounded-full bg-primary/10 px-1 text-[10px] font-semibold text-primary">
                        {c.unread}
                      </span>
                    ) : null}
                  </div>
                  <p className="truncate text-xs text-muted-foreground">
                    {c.lastMessage}
                  </p>
                </div>
              </div>
            </button>
          </li>
        ))}
      </ul>
    </ScrollArea>
  </aside>

<main className="flex flex-col min-h-screen">    
    {/* header */}
    <div className=" flex items-center justify-between gap-3  border-b p-3">
      <Avatar>
        <AvatarImage src=""/>
        <AvatarFallback className={"text-xs"}>
          {initials}
        </AvatarFallback>
      </Avatar>
      <div className="leading-tight">
        <div className="text-sm font-medium">
          {email.split("@")[0] || ""}
        </div>
      </div>
      <div className='flex gap-2'>
        <Button className='ml-auto' size="icon" variant="outline">
          <Search/>
        </Button>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button className='ml-auto' size="icon" variant="outline">
                <MoreVertical className="h-5 w-5" />
              </Button>
            </DropdownMenuTrigger>

            <DropdownMenuContent align="end">
              <DropdownMenuItem>Profile</DropdownMenuItem>
              <DropdownMenuItem>Settings</DropdownMenuItem>
              <DropdownMenuItem
                onClick={() =>
                  window.open(
                    "https://github.com/amarmaurya-com/HelpDeskAgent",
                    "_blank"
                  )
                }
              >
                GitHub
              </DropdownMenuItem>
              <DropdownMenuItem onClick={
                handleLogout
              } >Logout</DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
      </div>
    </div>


    {/* Chat area */}
      <ScrollArea className={"flex-1 h-[calc(100vh-120px)]"}>
        <div className="mx-auto max-w-3xl px-6 space-y-6">
          {
            messages.map((message, index) => (
              <MessageBubble key={index} author={message.author} at={message.at}>{message.text}</MessageBubble>
            ))
          }
        </div>
        <div ref={endRef} />
      </ScrollArea>

      {/* Composer */}
      <div className='border-t p-3'>
        <div 
        className='mx-auto flex max-w-3xl item-center gap-3'
        >
          <Input 
          onKeyDown={(e) => {
              if (e.key === "Enter") {
                sendMessage();
              }
              }}
          ref={inputRef}
          value={draft}
          onChange={(e) => setdraft(e.target.value)}
          placeholder="Type a problem..."
          calssName="rounded-3xl flex-1"

          />

          <Button 
          disabled={sending} 
          onClick={sendMessage}
          
          className={"rounded-2xl px-5"}
          >
          <span 
          className="sr-only">Send</span>{ !sending ? <Send className="h-4 w-4" /> : <Spinner data-icon="inline-start" />}</Button>
        </div>
      </div>
  </main>
</div>
  )
}

export default Chat
