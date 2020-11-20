import React from 'react'
import renderer from 'react-test-renderer'
import ChatroomCard from './ChatroomCard'

test('should be able to render chatroom card', () => {
    const chatroomMock = {chat: [],
                          genre: 'JAZZ',
                          link: '/joinroom/JAZZ',
                          participants: {'user1': {username: 'user1'}},
                          playlist: []}
    const component = renderer.create(
        <ChatroomCard chatroom={chatroomMock}/>,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
