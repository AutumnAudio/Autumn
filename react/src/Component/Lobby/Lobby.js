import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import { CircularProgress } from '@material-ui/core'
import ChatroomCardGrid from './ChatroomCardGrid'
import { getChatrooms } from '../AutumnApi/AutumnApi'
import { authenticate } from '../AutumnApi/AutumnApi'
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
    },
    circularProgress: {
        marginTop: '30px',
        marginLeft: '50%',
    },
}

const Lobby = (props) => {
    const { classes } = props
    const [chatrooms, setChatrooms] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    useEffect(() => {
        authenticate().then((response) => {
            console.log(response)
            return response.json()
        }).then((data) => {
            if ('error' in data) {
                const authURL = data['auth_url']
                window.location.href = authURL
            } else {
                getChatrooms().then((response) => {
                    return response.json()
                }).then((data) => {
                    setIsLoading(false)
                    let chatrooms = data['chatrooms']
                    
                    let chatroomsArray = []
                    for (let i in chatrooms) {
                        chatroomsArray.push(chatrooms[i])
                    }
                    console.log(chatroomsArray)
                    setChatrooms(chatroomsArray)
                })
            }
        })
        
    }, []);
    return (
        <div className={classes.lobby}>
            
            <div className={classes.headline}>
                Chatrooms:
            </div>
            {isLoading ? (<CircularProgress className={classes.circularProgress}/>) : null}
            <ChatroomCardGrid chatrooms={chatrooms} />
        </div>
    )
    
}
Lobby.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Lobby)