var express=require('express');
var bodyParser=require('body-parser');
var mongojs=require('mongojs');
var app= express();

db=mongojs('contactlist',['contactlist']);
app.use(express.static(__dirname+'/public'));
app.use(bodyParser.json());
app.listen(3000);


////////////////////////////////////////////////////////////////////////////////////
// GET ALL CONTACTS /////////////////////////////////////////////////////////////////////////

app.get('/contacts',function(req,res){
    db.contactlist.find(function(err, docs){
        
        res.json(docs);
    });
});
////////////////////////////////////////////////////////////////////////////////////
// ADD /////////////////////////////////////////////////////////////////////////

app.post('/contacts',function(req,res){
    db.contactlist.insert(req.body,function(err,doc){
        res.json(doc);
    })
});
////////////////////////////////////////////////////////////////////////////////////
// DELETE /////////////////////////////////////////////////////////////////////////

app.delete('/contacts/:id',function(req,res){
    db.contactlist.remove({_id:mongojs.ObjectId(req.params.id)},function(err,doc){
        res.json(doc);
    });
});

//EDIT///////////////////////////////////////////////////////////////////////////////
app.get('/contacts/:id',function(req,res){
    db.contactlist.findOne({_id:mongojs.ObjectId(req.params.id)},function(err,doc){
        res.json(doc);
    });
});
////////////////////////////////////////////////////////////////////////////////////
// UPDATE /////////////////////////////////////////////////////////////////////////
app.put('/contacts/:id',function(req,res){
    var id = req.params.id;
    db.contactlist.findAndModify({query:{_id:mongojs.ObjectId(id)},
                                 update:{$set:{name:req.body.name,email:req.body.email,phone:req.body.phone}}},                             
                                    function(err,doc){                                    
                                    res.json(doc);
    });
});
////////////////////////////////////////////////////////////////////////////////////

console.log("Server running in 3000");