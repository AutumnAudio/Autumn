import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ChatMessageList from './ChatMessageList'

const styles = {
    chatBoardDiv: {
        position: 'absolute',
        left: '30%',
        width: '70%',
        height: '90%',
        background: '#F3F2FF',
    }
};

const ChatBoard = (props) => {
    const { classes } = props;
    return (
        <div className={classes.chatBoardDiv}>
            <ChatMessageList />
        </div>
    )
}

ChatBoard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatBoard)