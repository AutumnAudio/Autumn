import React from 'react'
import renderer from 'react-test-renderer'
import PersonList from './PersonList'

test('should be able to render people list', () => {
    const participantsMock = [{username: 'testUser',
                        recentlyPlayed: [{name: 'song name', artists: ['testArtist']}]},
                        {username: 'testUser2',
                        recentlyPlayed: [{name: 'another song', artists: ['testArtist2']}]}]
    const component = renderer.create(
        <PersonList participants={participantsMock} />,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
