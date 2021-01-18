package com.ktpt.quna.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
//  @Retention이란, 어노테이션의 Life time이라고 생각하자.
//  Class
// 	바이트 코드 파일까지 어노테이션 정보를 유지한다.
// 	하지만 리플렉션을 이용해서 어노테이션 정보를 얻을 수는 없다.
// 	Runtime
// 	바이트 코드 파일까지 어노테이션 정보를 유지하면서 리플렉션을 이용해서 런타임시에 어노테이션 정보를 얻을 수 있다.
// 	Source
// 	Compile 이후로 삭제되는 형태
public @interface LoginMember {
}
