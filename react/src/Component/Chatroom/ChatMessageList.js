import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ChatMessage from './ChatMessage';

const styles = {
    
};

const ChatMessageList = (props) => {
    const { classes } = props;
    return (
        <div>
            <ChatMessage />
        </div>
    )
    
}

ChatMessageList.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatMessageList)