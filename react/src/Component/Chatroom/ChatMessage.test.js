import React from 'react'
import renderer from 'react-test-renderer'
import ChatMessage from './ChatMessage'

test('should be able to render chat', () => {
    const chatMock = {username: 'testUser',
                      message: 'some message'}
    const component = renderer.create(
        <ChatMessage message={chatMock} />,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
