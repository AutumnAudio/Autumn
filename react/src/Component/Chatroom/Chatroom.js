import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import PersonPanel from './PersonPanel';
import ChatBoard from './ChatBoard';
import TextBox from './TextBox'

const styles = {
    personPanelStyle: {
        width: '30%',
        height: '100%',
        background: '#1E2341',
        position: 'absolute',
    },
};

const Chatroom = (props) => {
    const { classes } = props;
    return (
        <div>
            <div className={classes.personPanelStyle}><PersonPanel /></div>
            
            <ChatBoard />
            <TextBox />
        </div>
    )
    
}
Chatroom.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Chatroom)