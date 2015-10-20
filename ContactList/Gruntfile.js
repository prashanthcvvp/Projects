module.exports =function(grunt){
    grunt.initConfig({
        concat: {
             js: {
                src: ['public/script/app.js',
                      'public/script/controllers/loginController.js',
                      'public/script/Services/services.js',
                      'public/script/controllers/ContactsController.js',
                      'public/script/controllers/registerController.js'
                     ],
                dest: 'public/script/build.js',
            },
         },
        watch:{
            scripts:{
                files:['public/script/**/*.js'],
                tasks:['concat','uglify']
            }
        },
        uglify: {
              minified: {
                files: {
                    'public/script/app.min.js': ['public/script/build.js']
                }
            }
        },
        express: {
            options: {
                // Override defaults here 
            },      
            dev: {
                options: {
                    script: 'server.js'
                }
            }
        },
        run: {
            options: {
                // Task-specific options go here. 
            },
            your_target: {
                cmd: 'mongod',
                args: []
            }
        }
        
    });
    
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-express-server');
    grunt.loadNpmTasks('grunt-run');
    
    grunt.registerTask('default',['concat','uglify','express','watch']);
    
}

