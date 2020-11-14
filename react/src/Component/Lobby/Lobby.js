import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ChatroomCardGrid from './ChatroomCardGrid'

const styles = {
    lobby: {
        position: 'absolute',
        left: '0',
        top: '0',
        width: '100%',
        height: '100%',
        background: '#1E2341',
    },
    headline: {
        marginLeft: '7%',
        marginTop: '40px',
        fontSize: '30px',
        color: 'white',
    }
};

const Lobby = (props) => {
    const { classes } = props;
    return (
        <div className={classes.lobby}>
            <div className={classes.headline}>Chatrooms:</div>
            <ChatroomCardGrid />
        </div>
    )
    
}
Lobby.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Lobby)