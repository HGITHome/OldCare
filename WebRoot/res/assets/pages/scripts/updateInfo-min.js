  var statu = 0;
  var editBut = document.getElementsByClassName("edit");
  var deleteBut = document.getElementsByClassName("delete");
  var desSpan = document.getElementsByClassName("des");
  var greSpan = document.getElementsByClassName("grade");
  var itemIds = document.getElementsByClassName("item_id");
  var typeIds = document.getElementsByClassName("type_id");
  
  var categoryEditBut = document.getElementsByClassName("categoryEdit");
  var categoryDeleteBut = document.getElementsByClassName("categoryDelete");
  var categorySpan = document.getElementsByClassName("fa fa-gift");
  var categoryIds = document.getElementsByClassName("categoryIds");
  
  var typeEditBut = document.getElementsByClassName("typeEdit");
  var typeDeleteBut = document.getElementsByClassName("typeDelete");
  var typeSpan = document.getElementsByClassName("typeSpan");
  var typesIds = document.getElementsByClassName("typeIds");
  
  for(var i =0;i<editBut.length;i++){
	 editBut[i].index=i;
	 editBut[i].statu=0;
	  editBut[i].onclick = function(){
		  if(this.statu == 0){
               this.value= '保存';
              // confirm('提现是否完成？');
               desSpan[this.index].innerHTML = '<input type="text" style="width:600px;border-radius:3px;border:1px solid #000;background-color:white;font-size:1em;height:1.7em;color:black"  class="item'+this.index+'"  value="'+desSpan[this.index].innerHTML+'">';
               greSpan[this.index].innerHTML = '<input type="text" style="width:30px;border-radius:3px;border:1px solid #000;background-color:white;font-size:1em;height:1.7em;color:black"  id="'+this.index+'"  value="'+greSpan[this.index].innerHTML+'">';
              this.statu = 1;
          }else{
              this.value= '编辑';
              var item_input = document.getElementsByClassName("item"+this.index);
             // desSpan[this.index].innerHTML = item_input[0].value;
              var grade_input = document.getElementById(this.index);
            //  greSpan[this.index].innerHTML = grade_input.value;
              
              var index=this.index;
              var xhr = getXhr();
              xhr.open("post","v_updateItem.do",true);
              xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
              xhr.send("id="+itemIds[this.index].value+"&description=" +item_input[0].value +"&grade="+grade_input.value+"&typeId="+typeIds[this.index].value);
              xhr.onreadystatechange = function(){
                  if(xhr.readyState==4&&xhr.status==200){
                	  //alert(item_input[0].value);
                	  var data = xhr.responseText;
                	   if(data != ''){ 
                		   alert("操作失败，请联系技术管理人!"); 
                	   }else{
                	      desSpan[index].innerHTML = item_input[0].value;
                	      greSpan[index].innerHTML = grade_input.value;
                	     
                	     alert("保存成功!"); 
                          location.reload();
                	   }
                      // 接收服务器端的数据内容
                     // alert("保存成功!");
                  }
             };
           //  window.localtion.href="u_updateItems.do";
              this.statu = 0;
             
          }
	  };
  }
  for(var i = 0;i<deleteBut.length;i++){
	  deleteBut[i].index=i;
	  deleteBut[i].onclick=function(){
		  if(window.confirm("确定删除吗?")){
		     var xhr = getXhr();
             xhr.open("post","v_deleteItem.do",true);
             xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             // alert(desSpan[this.index].innerHTML);
             xhr.send("id="+itemIds[this.index].value+"&description=" +desSpan[this.index].innerHTML +"&grade="+greSpan[this.index].innerHTML+"&typeId="+typeIds[this.index].value);
             xhr.onreadystatechange = function(){
                if(xhr.readyState==4&&xhr.status==200){
            	  //alert(item_input[0].value);
            	  var data = xhr.responseText;
            	   if(data != ''){ 
            		   alert("操作失败，请联系技术管理人!"); 
            	   }else{
            	   alert("删除成功!");
            	      location.reload();
            	     
            	   }
                  // 接收服务器端的数据内容
                 // alert("保存成功!");
              }
         };
             return true;
		  }else{
			  return false;
		  }
	  };
  }

  for(var i = 0; i < categoryEditBut.length; i++){
	  categoryEditBut[i].index = i;
	  categoryEditBut[i].statu = 0;
	  categoryEditBut[i].onclick = function(){
		  if(this.statu == 0){
              this.value= '保存';
              categorySpan[this.index].innerHTML = '<input type="text" style="width:120px;border-radius:3px;border:1px solid #000;background-color:white;font-size:1em;height:1.7em;color:black"  class="category'+this.index+'"  value="'+categorySpan[this.index].innerHTML+'">';
              this.statu = 1;
             
         }else{
             this.value= '编辑';
             var category_input = document.getElementsByClassName("category"+this.index);
             var index=this.index;
             var xhr = getXhr();
             xhr.open("post","v_updateCategory.do",true);
             xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             xhr.send("id="+categoryIds[this.index].value + "&name=" + category_input[0].value);
             xhr.onreadystatechange = function(){
                 if(xhr.readyState==4&&xhr.status==200){
               	  //alert(item_input[0].value);
               	  var data = xhr.responseText;
               	   if(data != ''){ 
               		   alert(data); 
               	   }else{
               	      categorySpan[index].innerHTML = category_input[0].value;
               	         alert("保存成功!"); 
                         location.reload();
               	   }
                     // 接收服务器端的数据内容
                    // alert("保存成功!");
                 }
            };
             this.statu = 0;
            
         }
	  };
  }
  for(var i = 0;i<categoryDeleteBut.length;i++){
	  categoryDeleteBut[i].index=i;
	  categoryDeleteBut[i].onclick=function(){
		  if(window.confirm("确定删除吗?")){
		     var xhr = getXhr();
             xhr.open("post","v_deleteCategory.do",true);
             xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             // alert(desSpan[this.index].innerHTML);
             xhr.send("id="+categoryIds[this.index].value);
             xhr.onreadystatechange = function(){
                if(xhr.readyState==4&&xhr.status==200){
            	  //alert(item_input[0].value);
            	  var data = xhr.responseText;
            	   if(data != ''){ 
            		   alert(data); 
            	   }else{
            	      alert("删除成功!");
            	      location.reload();
            	   }
                  // 接收服务器端的数据内容
                 // alert("保存成功!");
              }
         };
             return true;
		  }else{
			  return false;
		  }
	  };
  }
  
  
  for(var i = 0; i < typeEditBut.length; i ++){
	  typeEditBut[i].index = i;
	  typeEditBut[i].statu = 0;
	  typeEditBut[i].onclick = function(){
		  if(this.statu == 0){
              this.value= '保存';
              typeSpan[this.index].innerHTML = '<input type="text" style="width:120px;border-radius:3px;border:1px solid #000;background-color:white;font-size:1em;height:1.7em;color:black"  class="'+this.index+'"  value="'+typeSpan[this.index].innerHTML+'">';
             this.statu = 1;
         }else{
             this.value= '编辑';
             var type_input = document.getElementsByClassName(this.index);
             var index=this.index;
             var xhr = getXhr();
             xhr.open("post","v_updateType.do",true);
             xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             xhr.send("id="+typesIds[this.index].value + "&typeName=" + type_input[0].value);
             xhr.onreadystatechange = function(){
                 if(xhr.readyState==4&&xhr.status==200){
               	  //alert(item_input[0].value);
               	  var data = xhr.responseText;
               	   if(data != ''){ 
               		   alert(data); 
               	   }else{
               		typeSpan[index].innerHTML = type_input[0].value;
               	      alert("保存成功!"); 
                      location.reload();   
               	   }
                     // 接收服务器端的数据内容
                    // alert("保存成功!");
                 }
            };
          //  window.localtion.href="u_updateItems.do";
             this.statu = 0;
            
         }
	  };
  }
  
  for(var i = 0;i<typeDeleteBut.length;i++){
	  typeDeleteBut[i].index=i;
	  typeDeleteBut[i].onclick=function(){
		  if(window.confirm("确定删除吗?")){
		     var xhr = getXhr();
             xhr.open("post","v_deleteType.do",true);
             xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             // alert(desSpan[this.index].innerHTML);
             xhr.send("id="+typesIds[this.index].value);
             xhr.onreadystatechange = function(){
                if(xhr.readyState==4&&xhr.status==200){
            	  //alert(item_input[0].value);
            	  var data = xhr.responseText;
            	   if(data != ''){ 
            		   alert(data); 
            	   }else{
            	      alert("删除成功!");
            	      location.reload();
            	   }
                  // 接收服务器端的数据内容
                 // alert("保存成功!");
              }
         };
             return true;
		  }else{
			  return false;
		  }
	  };
  }
  
  function getXhr(){
	    var xhr = null;
	    if(window.XMLHttpRequest){
	        xhr = new XMLHttpRequest();
	    }else{
	    	//兼容ie6，只有ie6使用该以下方法来获取XMLHttpRequest对象
	        xhr = new ActiveXObject("Microsoft.XMLHttp");
	    }
	    return xhr;
	}