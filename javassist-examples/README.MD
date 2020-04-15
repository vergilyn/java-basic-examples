# javassist-examples

+ [javassist.org](http://www.javassist.org/)
+ [Github, javassist](https://github.com/jboss-javassist/javassist)
+ [javassist, tutorial.html](http://www.javassist.org/tutorial/tutorial.html)

- [javassist使用全解析](https://www.cnblogs.com/rickiyang/p/11336268.html)
+ [javassist tutorial2](http://www.javassist.org/tutorial/tutorial2.html)

<table border="0">
	<tbody>
		<tr>
			<td>
				<code>$0
				</code>, 
				<code>$1
				</code>, 
				<code>$2
				</code>, ... &nbsp; &nbsp;
			</td>
			<td>
				<code>this
				</code> and actual parameters
			</td>
		</tr>
		<tr>
			<td>
				<code>$args
				</code>
			</td>
			<td>An array of parameters.
				The type of 
				<code>$args
				</code> is 
				<code>Object[]
				</code>.
			</td>
		</tr>
		<tr>
			<td>
				<code>$$
				</code>
			</td>
			<td rowspan="2">All actual parameters.
				<br>
				For example, 
				<code>m($$)
				</code> is equivalent to
				<code>m($1,$2,
				</code>...
				<code>)
				</code>
			</td>
		</tr>
		<tr>
			<td>&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<code>$cflow(
				</code>...
				<code>)
				</code>
			</td>
			<td>
				<code>cflow
				</code> variable
			</td>
		</tr>
		<tr>
			<td>
				<code>$r
				</code>
			</td>
			<td>The result type.  It is used in a cast expression.
			</td>
		</tr>
		<tr>
			<td>
				<code>$w
				</code>
			</td>
			<td>The wrapper type.  It is used in a cast expression.
			</td>
		</tr>
		<tr>
			<td>
				<code>$_
				</code>
			</td>
			<td>The resulting value
			</td>
		</tr>
		<tr>
			<td>
				<code>$sig
				</code>
			</td>
			<td>An array of 
				<code>java.lang.Class
				</code> objects representing
				the formal parameter types.
			</td>
		</tr>
		<tr>
			<td>
				<code>$type
				</code>
			</td>
			<td>A 
				<code>java.lang.Class
				</code> object representing
				the formal result type.
			</td>
		</tr>
		<tr>
			<td>
				<code>$class
				</code>
			</td>
			<td>A 
				<code>java.lang.Class
				</code> object representing
				the class currently edited.
			</td>
		</tr>
	</tbody>
</table>

1. dubbo, Javassist
- [JavassistProxyFactory.java](https://github.com/apache/dubbo/blob/master/dubbo-rpc/dubbo-rpc-api/src/main/java/org/apache/dubbo/rpc/proxy/javassist/JavassistProxyFactory.java)
- [ClassGenerator.java](https://github.com/apache/dubbo/blob/master/dubbo-common/src/main/java/org/apache/dubbo/common/bytecode/ClassGenerator.java)