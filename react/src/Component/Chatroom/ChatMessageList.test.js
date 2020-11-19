import React from 'react'
import renderer from 'react-test-renderer'
import ChatMessageList from './ChatMessageList'

test('should be able to render chat message list', () => {
    const chatMock = [{username: 'testUser',
                      message: 'some message'},
                      {username: 'testUser2',
                      message: 'another message'}]
    const component = renderer.create(
        <ChatMessageList chat={chatMock} />,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
