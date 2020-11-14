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
    const { classes } = props;
    return (
        <div className={classes.personListDiv}><Person /></div>
    )
    
}

PersonList.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(PersonList)