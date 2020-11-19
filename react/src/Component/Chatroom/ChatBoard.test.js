import React from 'react'
import renderer from 'react-test-renderer'
import ChatBoard from './ChatBoard'

test('should be able to render chat board', () => {
    const chatMock = [{username: 'testUser',
                      message: 'some message'},
                      {username: 'testUser2',
                      message: 'another message'}]
    const component = renderer.create(
        <ChatBoard chat={chatMock} />,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
