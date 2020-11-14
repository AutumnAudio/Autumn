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
    )
    
}

PersonList.propTypes = {
    classes: PropTypes.object.isRequired,
<<<<<<< HEAD
    participants: PropTypes.array.isRequired,
=======
>>>>>>> add react prototype
};

export default withStyles(styles)(PersonList)