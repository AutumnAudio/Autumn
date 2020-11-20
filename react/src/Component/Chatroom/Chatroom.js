import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import PersonPanel from './PersonPanel';
import ChatBoard from './ChatBoard';
import TextBox from './TextBox'
import { useLocation } from "react-router-dom";

const styles = {
    personPanelStyle: {
        width: '30%',
        height: '100%',
        background: '#1E2341',
        position: 'absolute',
    },
};
let chatHistory = []
let participants = []
const updateprops = (msg, setParticipants, setChat, genre) => {
    let jsonData = JSON.parse(msg['data'])
    
    if ('username' in jsonData) {
        console.log(chatHistory)
        // it is a chat message
        console.log(participants)
        if (jsonData['username'] in participants) {
            console.log('handle')
            chatHistory.push(jsonData)
            const newChatHistory = [...chatHistory]
            setChat(newChatHistory)
        }
        return
    }
    if (genre !== jsonData['genre']) {
        return
    }
    
    participants = jsonData['participants']
    let participantsArray = []
    for (let i in participants) {
        participantsArray.push(participants[i])
    }
    setParticipants(participantsArray)
    
}
const Chatroom = (props) => {
    const { classes } = props;
    const [participants, setParticipants] = useState([])
    const [chat, setChat] = useState([])
    const location = useLocation()
    const genre  = location.state.genre
    useEffect(() => {
        
        let hostname = 'localhost'
        let port = '8080'
        chatHistory = []
        let ws = new WebSocket('ws://' + hostname + ':' + port + '/chatroom')
        ws.onmessage = msg => updateprops(msg, setParticipants, setChat, genre);
        ws.onclose = () => console.log("WebSocket connection closed")
        return () => {
            console.log('close')
            ws.close()
            chatHistory = []
        }
    }, []);
    
    return (
        <div>
            <div className={classes.personPanelStyle}><PersonPanel genre={genre} participants={participants}/></div>
            
            <ChatBoard chat={chat}/>
            <TextBox />
        </div>
    )
    
}
Chatroom.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Chatroom)