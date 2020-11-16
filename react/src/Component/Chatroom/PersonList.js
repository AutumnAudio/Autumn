import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Person from './Person';
const styles = {
    personListDiv: {
        marginTop: '30px',
    }
};

const PersonList = (props) => {
<<<<<<< HEAD
<<<<<<< HEAD
    const { classes, participants } = props;
    return (
        <div className={classes.personListDiv}>
            {participants.map((participant, index) => {
                return (<Person key={participant.username} person={participant}/>)
            })}
            
        </div>
=======
    const { classes } = props;
    return (
        <div className={classes.personListDiv}><Person /></div>
>>>>>>> add react prototype
=======
    const { classes, participants } = props;
    return (
        <div className={classes.personListDiv}>
            {participants.map((participant, index) => {
                return (<Person key={participant.username} person={participant}/>)
            })}
            
        </div>
>>>>>>> add websocket connection
    )
    
}

PersonList.propTypes = {
    classes: PropTypes.object.isRequired,
<<<<<<< HEAD
<<<<<<< HEAD
    participants: PropTypes.array.isRequired,
=======
>>>>>>> add react prototype
=======
    participants: PropTypes.array.isRequired,
>>>>>>> add websocket connection
};

export default withStyles(styles)(PersonList)