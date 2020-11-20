import React from 'react'
import ChatMessage from './ChatMessage'


const ChatMessageList = (props) => {
    const { chat } = props;
    return (
        <div>
            {chat.map((message, index) => {
                return <ChatMessage message={message} key={index}/>
            })}
        </div>
    )
    
}


export default ChatMessageList