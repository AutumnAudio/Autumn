import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ChatroomCardGrid from './ChatroomCardGrid'
import { getChatrooms } from '../AutumnApi/AutumnApi'

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
}

const Lobby = (props) => {
    const { classes } = props
    const [chatrooms, setChatrooms] = useState([])
    useEffect(() => {
        getChatrooms().then((response) => {
            return response.json()
        }).then((data) => {
            let chatrooms = data['chatrooms']
            
            let chatroomsArray = []
            for (let i in chatrooms) {
                chatroomsArray.push(chatrooms[i])
            }
            console.log(chatroomsArray)
            setChatrooms(chatroomsArray)
        })
    }, []);
    return (
        <div className={classes.lobby}>
            <div className={classes.headline}>Chatrooms:</div>
            <ChatroomCardGrid chatrooms={chatrooms} />
        </div>
    )
    
}
Lobby.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Lobby)