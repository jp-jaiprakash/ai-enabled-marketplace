import React, { useState, useEffect, useRef } from 'react';
import './index.css';
// Main App Component
export default function App() {
  // State to hold all chat messages.
  // We initialize it with a welcome message from the AI.
  const [messages, setMessages] = useState([
    {
      text: "Hello! I'm your store assistant. You can ask me about products, place an order, or check an order's status.",
      sender: 'ai',
    },
  ]);

  // State for the user's current input in the text box.
  const [inputValue, setInputValue] = useState('');
  
  // State to show a loading indicator while waiting for the AI's response.
  const [isLoading, setIsLoading] = useState(false);
  
  // A ref to the last message element, used to auto-scroll the chat view.
  const messagesEndRef = useRef(null);

  // Function to smoothly scroll to the bottom of the messages container.
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  // This effect runs whenever the 'messages' array changes, ensuring we always scroll down.
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Function to handle the form submission when a user sends a message.
  const handleSendMessage = async (e) => {
    e.preventDefault(); // Prevent default form submission which reloads the page.
    if (!inputValue.trim() || isLoading) return; // Don't send empty or while loading.

    // Add the user's message to the chat history immediately for a responsive feel.
    const userMessage = { text: inputValue, sender: 'user' };
    setMessages((prevMessages) => [...prevMessages, userMessage]);
    setInputValue(''); // Clear the input box.
    setIsLoading(true); // Show the loading indicator.

    try {
      // API call to our Spring Boot backend.
      // NOTE: For this to work in development, you need to set up a proxy in package.json
      // or ensure CORS is handled by the backend (which we did with @CrossOrigin).
      const response = await fetch('http://localhost:8080/api/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ message: inputValue }),
      });

      if (!response.ok) {
        throw new Error(`Network response was not ok: ${response.statusText}`);
      }

      const data = await response.json();
      
      // Add the AI's response to the chat history.
      const aiMessage = { text: data.response, sender: 'ai' };
      setMessages((prevMessages) => [...prevMessages, aiMessage]);

    } catch (error) {
      console.error('Failed to fetch AI response:', error);
      const errorMessage = { text: 'Sorry, I encountered an error connecting to the server. Please try again.', sender: 'ai' };
      setMessages((prevMessages) => [...prevMessages, errorMessage]);
    } finally {
      setIsLoading(false); // Hide the loading indicator.
    }
  };

  return (
    // Main container with a light gray background, centering the chat window.
    
    <div className="bg-gray-100 font-sans h-screen flex justify-center items-center p-4">
      <div className="bg-red-500 text-white p-10 text-3xl">Tailwind is working!</div>
      <div className="w-full max-w-2xl h-[95vh] flex flex-col bg-white rounded-2xl shadow-2xl">
        {/* Chat Header */}
        <div className="bg-blue-600 text-white p-4 rounded-t-2xl shadow-md flex items-center">
           <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-8 h-8 mr-3">
             <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 3v1.5M4.5 8.25H3m18 0h-1.5M4.5 12H3m18 0h-1.5m-15 3.75H3m18 0h-1.5M8.25 19.5V21M12 3v1.5m0 15V21m3.75-18v1.5m0 15V21m-9-1.5h10.5a2.25 2.25 0 0 0 2.25-2.25V6.75a2.25 2.25 0 0 0-2.25-2.25H6.75A2.25 2.25 0 0 0 4.5 6.75v10.5a2.25 2.25 0 0 0 2.25 2.25Zm.75-12h9v9h-9v-9Z" />
           </svg>
          <h1 className="text-xl font-bold">E-Commerce AI Assistant</h1>
        </div>

        {/* Chat Messages Container */}
        <div className="flex-1 p-6 overflow-y-auto">
          <div className="flex flex-col space-y-4">
            {messages.map((message, index) => (
              <div
                key={index}
                className={`flex items-end ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
              >
                <div
                  className={`max-w-xs md:max-w-md lg:max-w-lg px-4 py-3 rounded-2xl shadow ${
                    message.sender === 'user'
                      ? 'bg-blue-500 text-white rounded-br-none'
                      : 'bg-gray-200 text-gray-800 rounded-bl-none'
                  }`}
                >
                  {/* Using whitespace-pre-wrap to respect newlines in the response */}
                  <p className="whitespace-pre-wrap">{message.text}</p>
                </div>
              </div>
            ))}
            {/* Loading indicator shows up while waiting for a response */}
            {isLoading && (
              <div className="flex justify-start">
                  <div className="bg-gray-200 text-gray-800 rounded-2xl rounded-bl-none p-4 shadow">
                      <div className="flex items-center space-x-2">
                          <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-75"></div>
                          <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-150"></div>
                          <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-300"></div>
                      </div>
                  </div>
              </div>
            )}
            {/* This empty div is the target for our auto-scrolling ref */}
            <div ref={messagesEndRef} />
          </div>
        </div>

        {/* Message Input Form */}
        <div className="p-4 bg-white border-t rounded-b-2xl">
          <form onSubmit={handleSendMessage} className="flex items-center space-x-3">
            <input
              type="text"
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
              placeholder="Ask me anything..."
              className="flex-1 p-3 border rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              disabled={isLoading}
              autoFocus
            />
            <button
              type="submit"
              className="bg-blue-600 text-white p-3 rounded-full hover:bg-blue-700 disabled:bg-blue-300 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              disabled={isLoading || !inputValue.trim()}
            >
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M6 12 3.269 3.125A59.769 59.769 0 0 1 21.485 12 59.768 59.768 0 0 1 3.27 20.875L5.999 12Zm0 0h7.5" />
                </svg>
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
