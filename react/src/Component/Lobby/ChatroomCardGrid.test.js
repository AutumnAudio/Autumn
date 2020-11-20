import React from 'react'
import renderer from 'react-test-renderer'
import ChatroomCardGrid from './ChatroomCardGrid'

test('should be able to render chatroom card grid', () => {
    const chatroomsMock = [{chat: [],
                          genre: 'JAZZ',
                          link: '/joinroom/JAZZ',
                          participants: {'user1': {username: 'user1'}},
                          playlist: []},
                          {chat: [],
                            genre: 'POP',
                            link: '/joinroom/POP',
                            participants: {'user2': {username: 'user2'}},
                            playlist: []}]
    const component = renderer.create(
        <ChatroomCardGrid chatrooms={chatroomsMock}/>,
    )
    let tree = component.toJSON()
    expect(tree).toMatchSnapshot()
})
