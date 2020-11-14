import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ChatMessage from './ChatMessage';

const styles = {
    
};

const ChatMessageList = (props) => {
<<<<<<< HEAD
    const { classes, chat } = props;
    return (
        <div>
            {chat.map((message, index) => {
                return <ChatMessage message={message}/>
            })}
=======
    const { classes } = props;
    return (
        <div>
            <ChatMessage />
>>>>>>> add react prototype
        </div>
    )
    
}

ChatMessageList.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatMessageList)