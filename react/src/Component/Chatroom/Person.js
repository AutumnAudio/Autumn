import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import PersonIcon from '@material-ui/icons/Person';
import AddIcon from '@material-ui/icons/Add';

const styles = {
    personIcon: {
        position: 'relative',
        marginLeft: '10%',
        float: 'left',
        fontSize: '40px'
    },
    nameDiv: {
        positive: 'relative',
        marginLeft: '25%',
        fontSize: '20px',
    },
    currentSongDiv: {
        fontSize: '12px',
        position: 'relative',
        marginLeft: '25%',
        color: '#D0D1D7',
    },
    personDiv: {
        color: 'white',
        margin: '20px',
    },
    addIcon: {
        position: 'absolute',
        marginLeft: '270px',
        marginTop: '-10px',
    }
};

const Person = (props) => {
    const { classes } = props;
    return (
        <div>
            <div className={classes.personDiv}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <AddIcon className={classes.addIcon}/>
                <div className={classes.currentSongDiv}>Listening: watermelon sugar</div>
            </div>
            <div className={classes.personDiv}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <AddIcon className={classes.addIcon}/>
                <div className={classes.currentSongDiv}>Listening: watermelon sugar</div>
            </div>
            <div className={classes.personDiv}>
                <PersonIcon className={classes.personIcon}/>
                <div className={classes.nameDiv}>John Smith</div>
                <AddIcon className={classes.addIcon}/>
                <div className={classes.currentSongDiv}>Listening: watermelon sugar</div>
            </div>
        </div>
    )
    
}

Person.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Person)