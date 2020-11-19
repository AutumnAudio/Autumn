import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import PersonIcon from '@material-ui/icons/Person';

const styles = {
    personIcon: {
        float: 'left',
        fontSize: '47px',
        color: '#2699FB',
    },
    nameDiv: {
        fontWeight: 'bold',
        marginLeft: '55px',
        paddingTop: '5px',
    },
    textDiv: {
        marginLeft: '55px',
        marginTop: '5px',
        fontWeight: '300',
        wordWrap: 'break-word',
    },
    chatMessageList: {
        margin: '20px',
    },
};

const ChatMessage = (props) => {
    const { classes, message } = props;
    return (
        <div className={classes.chatMessageList}>
            <PersonIcon className={classes.personIcon}/>
            <div className={classes.nameDiv}>{message.username}</div>
            <div className={classes.textDiv}>{message.message}</div>
        </div>
    )
    
}

ChatMessage.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatMessage)