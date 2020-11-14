import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import PersonList from './PersonList';
const styles = {
    roomHeader: {
        margin: '25px',
        color: 'white',
        margin: '35px auto 0px 160px',
        display: 'inline-block',
    },
    arrowBack: {
        color: 'white',
        marginLeft: '25px',
    }
};

const PersonPanel = (props) => {
    const { classes } = props;
    return (
        <div>
            <ArrowBackIcon className={classes.arrowBack}/>
            <h1 className={classes.roomHeader}>Pop</h1>
            <PersonList />
        </div>
    )
    
}

PersonPanel.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(PersonPanel)