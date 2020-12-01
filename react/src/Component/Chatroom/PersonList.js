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
    const { classes, participants, updatePlayListSongs } = props;
    return (
        <div className={classes.personListDiv}>
            {participants.map((participant, index) => {
                return (<Person key={participant.username} person={participant} updatePlayListSongs={updatePlayListSongs}/>)
            })}
            
        </div>
    )
    
}

PersonList.propTypes = {
    classes: PropTypes.object.isRequired,
    participants: PropTypes.array.isRequired,
    updatePlayListSongs: PropTypes.func.isRequired,
};

export default withStyles(styles)(PersonList)