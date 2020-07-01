package org.zerock.domain;

import java.util.Date;

import lombok.Data;

@Data
public class BoardVO {
	
	private long bno; //bno number(10,0),
	private String title;//title varchar2(200) not null,
	private String content;//content varchar2(2000) not null,
	private String writer;//writer varchar2(50) not null,
	private Date regdate;//regdate date default sysdate,
	private Date updateDate;//updatedate date default sysdate
}
