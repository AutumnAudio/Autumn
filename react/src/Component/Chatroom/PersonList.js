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
    const { classes, participants } = props;
    return (
        <div className={classes.personListDiv}>
            {participants.map((participant, index) => {
                return (<Person key={participant.username} person={participant}/>)
            })}
            
        </div>
    )
    
}

PersonList.propTypes = {
    classes: PropTypes.object.isRequired,
    participants: PropTypes.array.isRequired,
};

export default withStyles(styles)(PersonList)