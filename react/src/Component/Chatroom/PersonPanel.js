import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import PersonList from './PersonList';
import { leaveRoom } from '../AutumnApi/AutumnApi'
import {
    useHistory,
  } from "react-router-dom"
const styles = {
    roomHeader: {
        color: 'white',
        margin: '35px auto 0px 160px',
        display: 'inline-block',
        fontFamily: 'Sansita Swashed',
    },
    arrowBack: {
        color: 'white',
        marginLeft: '25px',
    },
    personPanelDiv: {
        overflow: 'scroll',
        height: '100%',
        overflowX: 'hidden',
    }
}
const leaveChatroom = (genre, history) => {
    console.log(genre)
    leaveRoom(genre).then((response) => {
        return response.text()
    }).then((text) => {
        console.log(text)
        history.push('/lobby')
    })
        
}
const PersonPanel = (props) => {
    const { classes, participants, genre, updatePlayListSongs } = props
    const history = useHistory()
    useEffect(() => {
        return () => {
            leaveChatroom(genre, history)
        }
    }, [])
    return (
        <div className={classes.personPanelDiv}>
            <ArrowBackIcon className={classes.arrowBack} onClick={() => leaveChatroom(genre, history)}/>
            <h1 className={classes.roomHeader}>{genre}</h1>
            <PersonList participants={participants} updatePlayListSongs={updatePlayListSongs}/>
        </div>
    )
    
}

PersonPanel.propTypes = {
    classes: PropTypes.object.isRequired,
    participants: PropTypes.array.isRequired,
    genre: PropTypes.string.isRequired,
    updatePlayListSongs: PropTypes.func.isRequired,
};

export default withStyles(styles)(PersonPanel)