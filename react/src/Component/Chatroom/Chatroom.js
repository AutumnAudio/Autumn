import React, { useState, useEffect } from 'react';
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
const updateprops = (msg, setParticipants, setChat) => {
    let chatroom = JSON.parse(msg['data'])
    let participants = chatroom['participants']
    let chat = chatroom['chat']
    let participantsArray = []
    for (let i in participants) {
        participantsArray.push(participants[i])
    }
    setParticipants(participantsArray)
    setChat(chat)
}
const Chatroom = (props) => {
    const { classes } = props;
    const [participants, setParticipants] = useState([])
    const [chat, setChat] = useState([])
    let hostname = 'localhost'
    let port = '8080'
    useEffect(() => {
        let ws = new WebSocket('ws://' + hostname + ':' + port + '/chatroom')
        ws.onmessage = msg => updateprops(msg, setParticipants, setChat);
        ws.onclose = () => console.log("WebSocket connection closed")
    }, []);
    
    return (
        <div>
            <div className={classes.personPanelStyle}><PersonPanel participants={participants}/></div>
            
            <ChatBoard chat={chat}/>
            <TextBox />
        </div>
    )
    
}
Chatroom.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Chatroom)