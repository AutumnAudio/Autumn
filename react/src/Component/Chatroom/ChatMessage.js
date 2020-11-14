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
    },
    chatMessageList: {
        margin: '20px',
    },
};

const ChatMessage = (props) => {
    const { classes } = props;
    return (
        <div>
            <div className={classes.chatMessageList}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <div className={classes.textDiv}>Questions explained agreeable preferred strangers too him her son. Set put shyness offices his females him distant. Improve has message besides shy himself cheered however how son. Quick judge other leave ask first chief her. Indeed or remark always silent seemed narrow be. Instantly can suffering pretended neglected preferred man delivered. Perhaps fertile brandon do imagine to cordial cottage. </div>
            </div>
            <div className={classes.chatMessageList}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <div className={classes.textDiv}>Questions explained agreeable preferred strangers too him her son. Set put shyness offices his females him distant. Improve has message besides shy himself cheered however how son. Quick judge other leave ask first chief her. Indeed or remark always silent seemed narrow be. Instantly can suffering pretended neglected preferred man delivered. Perhaps fertile brandon do imagine to cordial cottage. </div>
            </div>
            <div className={classes.chatMessageList}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <div className={classes.textDiv}>Questions explained agreeable preferred strangers too him her son. Set put shyness offices his females him distant. Improve has message besides shy himself cheered however how son. Quick judge other leave ask first chief her. Indeed or remark always silent seemed narrow be. Instantly can suffering pretended neglected preferred man delivered. Perhaps fertile brandon do imagine to cordial cottage. </div>
            </div>
        </div>
    )
    
}

ChatMessage.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatMessage)