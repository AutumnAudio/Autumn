import React from 'react'
import PropTypes from 'prop-types'
import { withStyles } from '@material-ui/core/styles'

import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper'
import { joinRoom } from '../AutumnApi/AutumnApi'
import {
    useHistory,
  } from "react-router-dom"
const styles = {
    paper: {
        height: 180,
        width: 180,
        background: 'pink',
    },
    chatroomName: {
        paddingTop: '65%',
        paddingRight: '10px',
        fontSize: '25px',
        color: 'white',
        textAlign: 'right',
    }
}
const joinChatroom = (genre, history) => {
    console.log(genre)
    joinRoom(genre).then((response) => {
        return response.text()
    }).then((text) => {
        console.log(text)
        history.push('/chatroom', {genre: genre})
    })
    
}
const getLengthOfObject = (object) => {
    let total = 0
    for (let i in object) {
        total++
    }
    return total
}
const ChatroomCard = (props) => {
    const { classes, chatroom } = props;
    const genre = chatroom.genre
    const history = useHistory()
    console.log(chatroom)
    const participantsNumber = getLengthOfObject(chatroom.participants)
    return (
        <Grid item>
            <Paper className={classes.paper} onClick={() => joinChatroom(genre, history)}>
                <div>Online: {participantsNumber}</div> 
                <div className={classes.chatroomName}>{chatroom.genre}</div>
            </Paper>
        </Grid>
    )
    
}
ChatroomCard.propTypes = {
    classes: PropTypes.object.isRequired,
    chatroom: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatroomCard)