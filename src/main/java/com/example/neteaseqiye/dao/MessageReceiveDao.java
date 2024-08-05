package com.example.neteaseqiye.dao;

import com.example.neteaseqiye.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReceiveDao extends JpaRepository<Message,Long> {

}
