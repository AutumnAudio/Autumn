import React from 'react';
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
    },
    arrowBack: {
        color: 'white',
        marginLeft: '25px',
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
    const { classes, participants, genre } = props
    const history = useHistory()
    return (
        <div>
            <ArrowBackIcon className={classes.arrowBack} onClick={() => leaveChatroom(genre, history)}/>
            <h1 className={classes.roomHeader}>{genre}</h1>
            <PersonList participants={participants}/>
        </div>
    )
    
}

PersonPanel.propTypes = {
    classes: PropTypes.object.isRequired,
    participants: PropTypes.array.isRequired,
    genre: PropTypes.string.isRequired,
};

export default withStyles(styles)(PersonPanel)