import React from 'react'
import renderer from 'react-test-renderer'
import Person from './Person'

test('should be able to render people', () => {
    const personMock = {username: 'testUser',
                        recentlyPlayed: [{name: 'song name', artists: ['testArtist']}]}
    const component = renderer.create(
        <Person person={personMock} />,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
