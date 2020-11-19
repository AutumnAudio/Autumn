import React from 'react'
import renderer from 'react-test-renderer'
import PersonPanel from './PersonPanel'

test('should be able to render person panel', () => {
    const participantsMock = [{username: 'testUser',
                        recentlyPlayed: [{name: 'song name', artists: ['testArtist']}]},
                        {username: 'testUser2',
                        recentlyPlayed: [{name: 'another song', artists: ['testArtist2']}]}]
    const genreMock = 'POP'
    const component = renderer.create(
        <PersonPanel participants={participantsMock} genre={genreMock}/>,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
