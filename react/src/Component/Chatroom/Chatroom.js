import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import PersonPanel from './PersonPanel';
import ChatBoard from './ChatBoard';
import TextBox from './TextBox'
import PlayList from './PlayList'
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
    console.log(jsonData)
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
const updatePlayListSongs = (songs, name, setPlayListSongs, setIsPlayListOpen, setPlayListName) => {
    const songsArray = [...songs]
    console.log(songsArray)
    setPlayListSongs(songsArray)
    setIsPlayListOpen(true)
    setPlayListName(name)
}
const closePlayList = (setIsPlayListOpen) => {
    setIsPlayListOpen(false)
}
const Chatroom = (props) => {
    const { classes } = props;
    const [participants, setParticipants] = useState([])
    const [chat, setChat] = useState([])
    const [playListSongs, setPlayListSongs] = useState([])
    const [isPlayListOpen, setIsPlayListOpen] = useState(false)
    const [playListName, setPlayListName] = useState('')
    const location = useLocation()
    const genre  = location.state.genre
    useEffect(() => {
        
        let hostname = 'localhost'
        let port = '8080'
        chatHistory = []
        // let ws = new WebSocket('ws://' + hostname + ':' + port + '/chatroom')
        let ws = new WebSocket('ws://' + 'autumn-audio.herokuapp.com' + '/chatroom')
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
            <div className={classes.personPanelStyle}>
                <PersonPanel genre={genre} participants={participants} 
                  updatePlayListSongs={(songs, name) => updatePlayListSongs(songs, name, setPlayListSongs, setIsPlayListOpen, setPlayListName)}
                />
            </div>
            
            <ChatBoard chat={chat}/>
            {isPlayListOpen ? <PlayList songs={playListSongs} name={playListName} closePlayList={() => closePlayList(setIsPlayListOpen)}/> : null}
            <TextBox />
        </div>
    )
    
}
Chatroom.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Chatroom)