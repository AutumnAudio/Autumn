import React from 'react';
import PropTypes from 'prop-types'
import ChatMessage from './ChatMessage'


const ChatMessageList = (props) => {
    const { chat } = props;
    return (
        <div>
            {chat.map((message, index) => {
                return <ChatMessage message={message}/>
            })}
        </div>
    )
    
}

ChatMessageList.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default ChatMessageList